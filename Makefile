.PHONY: build up down logs rebuild ps clean

build:
	docker compose build

up:
	docker compose up -d

down:
	docker compose down

logs:
	docker compose logs -f

rebuild:
	docker compose up -d --build

ps:
	docker compose ps

clean:
	docker compose down -v --rmi local

backend-logs:
	docker compose logs -f backend

frontend-logs:
	docker compose logs -f frontend

ai-logs:
	docker compose logs -f ai-service

forecast-logs:
	docker compose logs -f forecast-service

mysql-shell:
	docker compose exec mysql mysql -uroot -proot streetleague

backend-shell:
	docker compose exec backend sh

frontend-shell:
	docker compose exec frontend sh

info:
	@echo "PI_StreetLeague — Docker Compose commands:"
	@echo "  make build       Build all images"
	@echo "  make up          Start all services"
	@echo "  make down        Stop all services"
	@echo "  make logs        Tail all logs"
	@echo "  make rebuild     Rebuild and restart"
	@echo "  make ps          List running containers"
	@echo "  make clean       Remove containers, volumes, images"
