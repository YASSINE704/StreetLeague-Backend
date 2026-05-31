FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt -q
COPY . .
EXPOSE 5001
ENTRYPOINT ["python", "app.py"]
