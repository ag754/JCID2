package cpp;

public class CrcPair {
    
    public CrcPair(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public long getValue() {
        return value;
    }

    private String key;
    private Long value;
}
