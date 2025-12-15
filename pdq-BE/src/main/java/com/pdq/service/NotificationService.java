package com.pdq.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.common.PageResponse;
import com.pdq.dto.notification.NotificationResponse;
import com.pdq.entity.Notification;
import com.pdq.entity.User;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.NotificationRepository;
import com.pdq.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                              UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public PageResponse<NotificationResponse> getMyNotifications(String userEmail, int page, int size) {
        User user = getUserByEmail(userEmail);
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return mapToPageResponse(notificationPage);
    }

    public long getUnreadCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        return notificationRepository.countUnreadByUser(user);
    }

    @Transactional
    public NotificationResponse markAsRead(String userEmail, Long notificationId) {
        User user = getUserByEmail(userEmail);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        // Check ownership
        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Access denied");
        }

        notification.setIsRead(true);
        notification = notificationRepository.save(notification);

        return mapToResponse(notification);
    }

    @Transactional
    public void markAllAsRead(String userEmail) {
        User user = getUserByEmail(userEmail);
        notificationRepository.markAllAsReadByUser(user);
    }

    @Transactional
    public void deleteNotification(String userEmail, Long notificationId) {
        User user = getUserByEmail(userEmail);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        // Check ownership
        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Access denied");
        }

        notificationRepository.delete(notification);
    }

    @Transactional
    public void createNotification(User user, String type, String title, String message,
                                   Long referenceId, String referenceType) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setReferenceId(referenceId);
        notification.setReferenceType(referenceType);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);
    }

    // Helper methods to create specific notifications
    @Transactional
    public void notifyOrderCreated(User user, String orderNumber) {
        createNotification(
            user,
            "order",
            "Đơn hàng đã được tạo",
            "Đơn hàng " + orderNumber + " của bạn đã được tạo thành công",
            null,
            "order"
        );
    }

    @Transactional
    public void notifyOrderStatusChanged(User user, String orderNumber, String status) {
        createNotification(
            user,
            "order",
            "Cập nhật đơn hàng",
            "Đơn hàng " + orderNumber + " đã chuyển sang trạng thái: " + status,
            null,
            "order"
        );
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(notification.getNotificationId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setReferenceId(notification.getReferenceId());
        response.setReferenceType(notification.getReferenceType());
        response.setIsRead(notification.getIsRead());
        response.setImageUrl(notification.getImageUrl());
        response.setActionUrl(notification.getActionUrl());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }

    private PageResponse<NotificationResponse> mapToPageResponse(Page<Notification> page) {
        List<NotificationResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}