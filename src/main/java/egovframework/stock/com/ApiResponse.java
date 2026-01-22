package egovframework.stock.com;

import java.util.List;
import java.util.Map;

public class ApiResponse {

	private List<Map<String, Object>> items;

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}
