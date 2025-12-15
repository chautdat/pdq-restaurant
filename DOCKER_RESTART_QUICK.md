# 🚀 DOCKER RESTART - QUICK GUIDE

## 📋 LỆNH NHANH

### **Sửa Frontend:**
```bash
docker-compose up -d --build frontend
```

### **Sửa Backend:**
```bash
docker-compose up -d --build backend
```

### **Import SQL mới:**
```bash
docker-compose stop backend && \
docker exec -it pdq-mysql mysql -u root -proot -e "DROP DATABASE IF EXISTS pdq_restaurant; CREATE DATABASE pdq_restaurant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" && \
docker exec -i pdq-mysql mysql -u root -proot pdq_restaurant < ~/DoAn/DACN/docker/init-scripts/pdq_restaurant.sql && \
docker-compose start backend
```

### **Restart nhanh:**
```bash
docker-compose restart
```

### **Rebuild tất cả:**
```bash
docker-compose down
docker-compose up -d --build
```

### **Reset hoàn toàn (⚠️ XÓA DATA):**
```bash
docker-compose down -v
docker-compose up -d --build
```

---

## 📊 XEM LOGS

```bash
# Tất cả
docker-compose logs -f

# Backend
docker-compose logs -f backend

# Frontend  
docker-compose logs -f frontend
```

**Nhấn `Ctrl+C` để thoát**

---

## ⚡ ALIAS (Thêm vào ~/.zshrc)

```bash
alias dc-fe='cd ~/DoAn/DACN/docker && docker-compose up -d --build frontend'
alias dc-be='cd ~/DoAn/DACN/docker && docker-compose up -d --build backend'
alias dc-restart='cd ~/DoAn/DACN/docker && docker-compose restart'
alias dc-logs='cd ~/DoAn/DACN/docker && docker-compose logs -f'
alias dc-reset='cd ~/DoAn/DACN/docker && docker-compose down -v && docker-compose up -d --build'
```

Sau đó: `source ~/.zshrc`

**Dùng:**
```bash
dc-fe       # Rebuild frontend
dc-be       # Rebuild backend
dc-restart  # Restart nhanh
dc-logs     # Xem logs
dc-reset    # Reset toàn bộ
```

---

## 🎯 BẢNG TÓM TẮT

| Sửa gì? | Lệnh | Thời gian |
|---------|------|-----------|
| Frontend | `docker-compose up -d --build frontend` | ~30s |
| Backend | `docker-compose up -d --build backend` | ~1-2m |
| SQL | Xem lệnh import ở trên | ~10s |
| Restart nhanh | `docker-compose restart` | ~30s |
| Rebuild tất cả | `docker-compose down && docker-compose up -d --build` | ~3-5m |

---

**🎉 Done!**
