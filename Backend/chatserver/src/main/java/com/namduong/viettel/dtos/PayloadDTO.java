package com.namduong.viettel.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PayloadDTO {
    private String type;
    private String url;
    private String sticker_id;
}
