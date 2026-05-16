from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
from statsmodels.tsa.statespace.sarimax import SARIMAX
from statsmodels.tsa.arima.model import ARIMA
import warnings
warnings.filterwarnings('ignore')

app = Flask(__name__)
CORS(app)

def fit_sarima(series: pd.Series, forecast_days: int = 7):
    """
    Fit SARIMA(1,1,1)(1,1,1,7) on daily reservation counts.
    Falls back to ARIMA(1,1,1) if not enough data for seasonal component.
    """
    n = len(series)

    if n < 14:
        # Not enough data — use simple moving average extrapolation
        avg = series.mean() if n > 0 else 0
        trend = 0
        if n >= 2:
            trend = (series.iloc[-1] - series.iloc[0]) / max(n - 1, 1)
        predictions = []
        for i in range(1, forecast_days + 1):
            val = max(0, round(avg + trend * i))
            predictions.append(val)
        return predictions, "moving_average"

    try:
        if n >= 21:
            # Enough data for seasonal ARIMA with weekly seasonality (m=7)
            model = SARIMAX(
                series,
                order=(1, 1, 1),
                seasonal_order=(1, 1, 1, 7),
                enforce_stationarity=False,
                enforce_invertibility=False
            )
            model_name = "SARIMA(1,1,1)(1,1,1,7)"
        else:
            # Use ARIMA without seasonal component
            model = ARIMA(series, order=(1, 1, 1))
            model_name = "ARIMA(1,1,1)"

        result = model.fit(disp=False)
        forecast = result.forecast(steps=forecast_days)
        predictions = [max(0, round(float(v))) for v in forecast]
        return predictions, model_name

    except Exception as e:
        # Fallback to moving average
        avg = series.mean()
        predictions = [max(0, round(float(avg)))] * forecast_days
        return predictions, f"fallback_moving_average (error: {str(e)[:50]})"


@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "ok", "service": "SARIMA Forecast Service"})


@app.route('/predict', methods=['POST'])
def predict():
    """
    Expects JSON:
    {
      "history": [
        {"date": "2026-04-01", "count": 3},
        {"date": "2026-04-02", "count": 1},
        ...
      ],
      "forecast_days": 7
    }
    Returns:
    {
      "forecast": [
        {"date": "2026-04-08", "predicted": 4},
        ...
      ],
      "model": "SARIMA(1,1,1)(1,1,1,7)",
      "history_points": 30
    }
    """
    data = request.get_json()
    history = data.get('history', [])
    forecast_days = data.get('forecast_days', 7)

    if not history:
        return jsonify({"error": "No history data provided"}), 400

    # Build time series
    df = pd.DataFrame(history)
    df['date'] = pd.to_datetime(df['date'])
    df = df.sort_values('date').set_index('date')

    # Fill missing days with 0
    full_range = pd.date_range(start=df.index.min(), end=df.index.max(), freq='D')
    df = df.reindex(full_range, fill_value=0)
    series = df['count'].astype(float)

    # Fit model and forecast
    predictions, model_name = fit_sarima(series, forecast_days)

    # Build forecast dates (next N days after last history date)
    last_date = df.index.max()
    forecast_dates = pd.date_range(start=last_date + pd.Timedelta(days=1), periods=forecast_days, freq='D')

    forecast_result = [
        {"date": str(d.date()), "predicted": int(v)}
        for d, v in zip(forecast_dates, predictions)
    ]

    return jsonify({
        "forecast": forecast_result,
        "model": model_name,
        "history_points": len(series)
    })


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=False)
