package com.ideamanagement.service.impl;

import com.ideamanagement.dto.SubscriptionDto;
import com.ideamanagement.entity.Subscription;
import com.ideamanagement.entity.User;
import com.ideamanagement.repository.SubscriptionRepository;
import com.ideamanagement.repository.UserRepository;
import com.ideamanagement.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubscriptionDto createSubscription(SubscriptionDto subscriptionDto) {
        User user = userRepository.findById(subscriptionDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (subscriptionRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("User already has a subscription");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(subscriptionDto.getPlan());
        subscription.setStatus(subscriptionDto.getStatus());
        subscription.setStartedAt(subscriptionDto.getStartedAt());
        subscription.setExpiresAt(subscriptionDto.getExpiresAt());

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionDto getSubscription(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return convertToDto(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionDto getSubscriptionByUser(UUID userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return convertToDto(subscription);
    }

    @Override
    @Transactional
    public SubscriptionDto updateSubscription(UUID id, SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setPlan(subscriptionDto.getPlan());
        subscription.setStatus(subscriptionDto.getStatus());
        subscription.setStartedAt(subscriptionDto.getStartedAt());
        subscription.setExpiresAt(subscriptionDto.getExpiresAt());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(updatedSubscription);
    }

    @Override
    @Transactional
    public SubscriptionDto updateSubscriptionStatus(UUID id, Subscription.StatusType status) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(status);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(updatedSubscription);
    }

    @Override
    @Transactional
    public SubscriptionDto updateSubscriptionPlan(UUID id, Subscription.PlanType plan) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setPlan(plan);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(updatedSubscription);
    }

    @Override
    @Transactional
    public void deleteSubscription(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscriptionRepository.delete(subscription);
    }

    private SubscriptionDto convertToDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setPlan(subscription.getPlan());
        dto.setStatus(subscription.getStatus());
        dto.setStartedAt(subscription.getStartedAt());
        dto.setExpiresAt(subscription.getExpiresAt());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());
        return dto;
    }
} 