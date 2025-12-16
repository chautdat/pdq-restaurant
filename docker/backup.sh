#!/bin/bash

###############################################################################
# PDQ Restaurant - Local Database Backup Script
# Backup database từ Docker container
###############################################################################

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}  PDQ Restaurant - Database Backup${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo ""

# Configuration
CONTAINER_NAME="pdq-mysql"
DB_NAME="pdq_restaurant"
DB_USER="pdq_user"
DB_PASSWORD="pdq_password"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="./mysql/backups"
BACKUP_FILE="pdq_backup_${TIMESTAMP}.sql"

# Tạo thư mục backup
mkdir -p ${BACKUP_DIR}

# Check container đang chạy
echo -e "${BLUE}🔍 Checking MySQL container...${NC}"
if ! docker ps | grep -q ${CONTAINER_NAME}; then
    echo -e "${RED}❌ Container ${CONTAINER_NAME} is not running!${NC}"
    echo -e "${YELLOW}Start it with: docker-compose up -d mysql${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Container is running${NC}"
echo ""

# Backup
echo -e "${BLUE}💾 Creating backup...${NC}"
docker exec ${CONTAINER_NAME} mysqldump \
  -u ${DB_USER} \
  -p${DB_PASSWORD} \
  ${DB_NAME} \
  --single-transaction \
  --routines \
  --triggers \
  --events \
  > ${BACKUP_DIR}/${BACKUP_FILE}

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Backup created successfully${NC}"
else
    echo -e "${RED}❌ Backup failed!${NC}"
    exit 1
fi

# Compress
echo -e "${BLUE}🗜️  Compressing backup...${NC}"
gzip ${BACKUP_DIR}/${BACKUP_FILE}
echo -e "${GREEN}✓ Backup compressed${NC}"
echo ""

# Info
BACKUP_SIZE=$(du -h ${BACKUP_DIR}/${BACKUP_FILE}.gz | cut -f1)
echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ Backup Completed Successfully!${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo ""
echo -e "📦 File: ${BACKUP_FILE}.gz"
echo -e "📁 Location: ${BACKUP_DIR}/"
echo -e "📊 Size: ${BACKUP_SIZE}"
echo -e "⏰ Timestamp: ${TIMESTAMP}"
echo ""

# Cleanup old backups (keep last 7)
echo -e "${BLUE}🗑️  Cleaning old backups (keeping last 7)...${NC}"
cd ${BACKUP_DIR}
ls -t *.sql.gz 2>/dev/null | tail -n +8 | xargs -r rm
BACKUP_COUNT=$(ls -1 *.sql.gz 2>/dev/null | wc -l)
echo -e "${GREEN}✓ Total backups: ${BACKUP_COUNT}${NC}"
echo ""

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}Done! 🎉${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"