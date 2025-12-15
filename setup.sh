#!/bin/bash

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

clear
echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   PDQ Restaurant - Setup Wizard       ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════╝${NC}"
echo ""

# Kiểm tra .env
if [ -f "docker/.env" ]; then
    echo -e "${YELLOW}⚠️  File .env đã tồn tại!${NC}"
    read -p "Tạo lại? (y/N): " -n 1 -r
    echo
    [[ ! $REPLY =~ ^[Yy]$ ]] && exit 0
fi

echo -e "${GREEN}Chọn chế độ setup:${NC}"
echo "1) 🚀 Quick Setup (Demo API key - Limited features)"
echo "2) 🔐 Custom Setup (Enter your own API key)"
echo ""
read -p "Lựa chọn (1-2): " choice

case $choice in
    2)
        echo ""
        echo -e "${BLUE}Nhập API keys:${NC}"
        echo -e "${YELLOW}💡 Lấy API key tại: https://console.groq.com/keys${NC}"
        read -p "GROQ_API_KEY: " groq_key
        
        if [ -z "$groq_key" ]; then
            echo -e "${RED}❌ API key không được để trống!${NC}"
            exit 1
        fi
        ;;
    *)
        echo ""
        echo -e "${YELLOW}⚠️  Dùng Demo Key (Giới hạn chức năng)${NC}"
        echo -e "${YELLOW}📧 Liên hệ: chautdat@example.com để lấy Production Key${NC}"
        groq_key="DEMO_KEY_CONTACT_ADMIN_FOR_PRODUCTION"
        ;;
esac

# Tạo .env
cat > docker/.env << ENVFILE
# ==========================================
# PDQ Restaurant - Environment Variables
# ==========================================
# Generated: $(date)

# API Keys
GROQ_API_KEY=$groq_key

# Database
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=pdq_restaurant
MYSQL_USER=pdq_user
MYSQL_PASSWORD=pdq_password

# RabbitMQ
RABBITMQ_USER=admin
RABBITMQ_PASSWORD=admin123

# Redis
REDIS_PASSWORD=redis123
ENVFILE

echo ""
echo -e "${GREEN}✅ Setup hoàn tất!${NC}"
echo ""
echo -e "${BLUE}═══════════════════════════════════════${NC}"
echo -e "${BLUE}Bước tiếp theo:${NC}"
echo ""
echo "  cd docker"
echo "  docker-compose up -d"
echo ""
echo -e "${BLUE}═══════════════════════════════════════${NC}"
echo -e "${BLUE}Access URLs:${NC}"
echo ""
echo "  🌐 Frontend:  http://localhost:8080"
echo "  ⚡ Backend:   http://localhost:3000"
echo "  🐰 RabbitMQ:  http://localhost:15672"
echo "     Username: admin"
echo "     Password: admin123"
echo ""
echo -e "${BLUE}═══════════════════════════════════════${NC}"
echo ""