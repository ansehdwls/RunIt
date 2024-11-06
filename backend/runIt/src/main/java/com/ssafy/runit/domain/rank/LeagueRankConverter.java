package com.ssafy.runit.domain.rank;

import jakarta.persistence.AttributeConverter;

public class LeagueRankConverter implements AttributeConverter<LeagueRank, Integer> {
    @Override
    public Integer convertToDatabaseColumn(LeagueRank attribute) {
        if (attribute == null) {
            return -1;
        }
        return attribute.getRank();
    }

    @Override
    public LeagueRank convertToEntityAttribute(Integer dbData) {
        if (dbData < 1) {
            return null;
        }
        return LeagueRank.fromRank(dbData);
    }

}
