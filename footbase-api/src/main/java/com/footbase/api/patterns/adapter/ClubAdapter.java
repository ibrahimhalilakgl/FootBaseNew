package com.footbase.api.patterns.adapter;

import com.footbase.api.patterns.factory.ExternalClubDto;

import java.util.Map;

/**
 * Adapter arayüzü: farklı dış veri formatlarını iç DTO'ya dönüştürür.
 */
public interface ClubAdapter {
    ExternalClubDto adapt(Map<String, Object> raw);
}
