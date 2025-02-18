package com.ssafy.pickitup.domain.user.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "user")
public class UserMongo {

    @Id
    private Integer id;

    private List<String> keywords;
    private String rank;
    private double latitude;
    private double longitude;

}