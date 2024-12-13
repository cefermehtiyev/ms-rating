package az.ingress.model.constants;

import liquibase.pro.packaged.G;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheKey {

    public static final String CACHE_KEY = "ms-rating:product-id:";
}
