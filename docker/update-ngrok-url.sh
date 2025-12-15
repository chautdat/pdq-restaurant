#!/bin/bash

echo "=========================================="
echo "🔄 AUTO UPDATE NGROK URL"
echo "=========================================="
echo ""

# Đợi ngrok khởi động
echo "⏳ Waiting for ngrok to start..."
sleep 8

# Lấy URL từ ngrok API
echo "🔍 Getting ngrok URL..."
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o 'https://[^"]*ngrok-free.dev' | head -1)

if [ -z "$NGROK_URL" ]; then
    echo "❌ Cannot get ngrok URL!"
    echo "   Make sure ngrok container is running: docker-compose ps"
    exit 1
fi

echo "✅ Ngrok URL: $NGROK_URL"
echo ""

# Backup file cũ
cp docker-compose.yml docker-compose.yml.backup

# Cập nhật URL trong docker-compose.yml
echo "📝 Updating docker-compose.yml..."

if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' "s|VNPAY_RETURN_URL:.*|VNPAY_RETURN_URL: ${NGROK_URL}/api/payment/vnpay/callback|" docker-compose.yml
    sed -i '' "s|ZALOPAY_CALLBACK_URL:.*|ZALOPAY_CALLBACK_URL: ${NGROK_URL}/api/payment/zalopay/callback|" docker-compose.yml
else
    # Linux
    sed -i "s|VNPAY_RETURN_URL:.*|VNPAY_RETURN_URL: ${NGROK_URL}/api/payment/vnpay/callback|" docker-compose.yml
    sed -i "s|ZALOPAY_CALLBACK_URL:.*|ZALOPAY_CALLBACK_URL: ${NGROK_URL}/api/payment/zalopay/callback|" docker-compose.yml
fi

echo "✅ Updated URLs in docker-compose.yml"
echo ""

# Restart backend để áp dụng URL mới
echo "🔄 Restarting backend..."
docker-compose restart backend

echo ""
echo "=========================================="
echo "✅ COMPLETED!"
echo "=========================================="
echo "🌐 Frontend: http://localhost:8080"
echo "🔗 Backend API: http://localhost:3000"
echo "📊 Ngrok Dashboard: http://localhost:4040"
echo "🔗 Ngrok Public URL: $NGROK_URL"
echo "=========================================="