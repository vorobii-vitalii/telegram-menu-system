package lpnu.telegram.menu.system.dto;

import lombok.Builder;

@Builder
public record FullCategoryDetail(String categoryId, String parentCategoryId, String name) {
}
