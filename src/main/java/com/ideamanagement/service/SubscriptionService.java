package com.ideamanagement.service;

import com.ideamanagement.dto.SubscriptionDto;
import com.ideamanagement.entity.Subscription;

import java.util.UUID;

public interface SubscriptionService {
    SubscriptionDto createSubscription(SubscriptionDto subscriptionDto);
    SubscriptionDto getSubscription(UUID id);
    SubscriptionDto getSubscriptionByUser(UUID userId);
    SubscriptionDto updateSubscription(UUID id, SubscriptionDto subscriptionDto);
    SubscriptionDto updateSubscriptionStatus(UUID id, Subscription.StatusType status);
    SubscriptionDto updateSubscriptionPlan(UUID id, Subscription.PlanType plan);
    void deleteSubscription(UUID id);
} 