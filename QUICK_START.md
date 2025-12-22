# 📋 Quick Start Guide - Distance-Based Shipping

## What Changed?

Your restaurant ordering system now calculates shipping fees **based on delivery distance** instead of using a fixed fee.

### Old System:

```
Always: 15,000 VND (except free for orders > 200k)
```

### New System:

```
Distance ≤ 3 km:     15,000 VND
Distance 4 km:       20,000 VND
Distance 5 km:       25,000 VND
Distance 5.5 km:     30,000 VND (rounded)
Distance 7 km:       35,000 VND
...
Plus: Always free for orders > 200,000 VND
```

---

## Testing the Feature

### Test Case 1: Close Delivery (< 3 km)

1. Go to Checkout page
2. Enter address: **Nearby location (within 3 km of restaurant)**
3. Expected:
   - Distance shows: ~2.5 km
   - Shipping fee: 15,000 VND ✅

### Test Case 2: Medium Distance (4-6 km)

1. Enter address: **Moderate location (4-6 km away)**
2. Expected:
   - Distance shows: ~5.2 km
   - Shipping fee: ~27,500 → 30,000 VND (rounded) ✅

### Test Case 3: Farther Delivery (7+ km)

1. Enter address: **Far location (7+ km away)**
2. Expected:
   - Distance shows: ~8.5 km
   - Shipping fee: 15,000 + (5.5 × 5,000) = 42,500 → 40,000 VND ✅

### Test Case 4: Free Shipping (High Order Value)

1. Enter expensive address
2. Add items totaling > 200,000 VND
3. Expected:
   - Distance shows: Any distance
   - Shipping fee: 0 VND (free) ✅

### Test Case 5: GPS Location

1. Click "Dùng vị trí hiện tại" (Use current location)
2. Allow browser GPS permission
3. Expected:
   - Address autocompletes from coordinates
   - Distance calculates automatically ✅

---

## Visual Indicators

### Before Address Selection:

```
Tạm tính (3 món):     135,000 ₫
Phí giao hàng:        15,000 ₫
Tổng cộng:            150,000 ₫
```

### After Address Selection (New):

```
Tạm tính (3 món):           135,000 ₫
📍 Khoảng cách: 4.52 km     ← NEW DISPLAY
Phí giao hàng:              20,000 ₫
Tổng cộng:                  155,000 ₫
```

---

## How Distance is Calculated

### Frontend (Instant Display):

- Uses **Haversine formula** (mathematical calculation)
- Works instantly in browser
- Shows as user types address

### Backend (Verification):

- Uses **Google Maps Distance Matrix API** (real-world routing)
- More accurate (considers actual roads)
- Calculates when order is submitted
- Updates database with real distance

Both calculations should be very close (within 5-10%) for short distances.

---

## Database

### Order Table Gets:

```sql
-- New/Updated Columns
distance:     DOUBLE          -- Distance in km from restaurant
shipping_fee: DECIMAL(10,2)   -- Final shipping fee in VND
```

### View Order History:

Go to **Orders** → Click any order → See:

- Distance: 4.52 km
- Shipping Fee: 20,000 VND

---

## Configuration (if you need to change)

### Restaurant Location:

```javascript
// In Checkout.vue - calculateSummary() method
const RESTAURANT_LAT = 10.855232;
const RESTAURANT_LNG = 106.78578;
// Change these to your actual restaurant coordinates
```

### Shipping Rates:

```javascript
// In Checkout.vue - calculateShippingFee() method
const BASE_FEE = 15000; // Fee for ≤3 km
const EXTRA_PER_KM = 5000; // Fee per km > 3 km
// Change these to adjust pricing
```

Same in backend:

```java
// In OrderService.java
private static final BigDecimal DELIVERY_FEE = new BigDecimal("15000");
// Change if needed
```

---

## Troubleshooting

### Issue: Distance not showing

- **Solution:** Make sure you selected address with coordinates (not just typed text)
- Try: Use GPS button or autocomplete from dropdown

### Issue: Distance wrong

- **Solution:** Google Maps API might be unavailable on backend
- Frontend calculation is still accurate
- Check browser console for errors

### Issue: Shipping fee not updating when address changes

- **Solution:** Browser cache might be holding old value
- Try: Refresh page or clear browser cache

### Issue: Order saved with wrong distance

- **Solution:** Check Google Maps API key in `.env`
- If not configured, backend uses default fee

---

## For Admin Dashboard

### Monitor Shipping:

1. Go to **Orders** admin page
2. View any order
3. See calculated distance and fee

### Future Updates:

- Could add admin panel to change rates
- Could show delivery zone map
- Could add time-based surcharges

---

## What's Next?

Current implementation is **complete and working**.

Future enhancements could include:

- Real-time tracking overlay
- Estimated delivery time display
- Zone-based pricing (downtown vs suburbs)
- Peak hour surcharges
- Shipping insurance options

---

## Questions?

All changes are in:

- **Frontend:** `frontend/src/pages/Checkout.vue`
- **Backend:** `pdq-BE/src/main/java/com/pdq/service/OrderService.java`
- **Docs:** `SHIPPING_FEE_SPEC.md` and `IMPLEMENTATION_SUMMARY.md`
