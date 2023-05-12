package com.ascertain.mockdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product implements Serializable {
    @Id
    @SequenceGenerator(
            name= "product_id_sequence",
            sequenceName = "product_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_id_sequence"
    )
    @JsonIgnore
    private Integer id;
    private String name;
    private String code;
    private double price;

    public static List<Product> buildProductsFromJson(JSONObject object){
        List<Product> products = new ArrayList<>();
        JSONArray jsonArray = object.getJSONArray("products");
        for (int i = 0; i < jsonArray.length(); i++) {
            String code = jsonArray.getJSONObject(i).getString("code");
            String name = jsonArray.getJSONObject(i).getString("name");
            double price = jsonArray.getJSONObject(i).getDouble("price");
            Product product = Product.builder()
                    .code(code)
                    .name(name)
                    .price(price)
                    .build();
            products.add(product);
        }
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (Double.compare(product.getPrice(), getPrice()) != 0) return false;
        if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
        return getCode() != null ? getCode().equals(product.getCode()) : product.getCode() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
