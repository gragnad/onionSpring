package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    /**
     *
     * @param queryString / URL ? 이후에 전달되는 형식 GET Method
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     *
     * @param cookies / name=value; name2=value cookie 타입
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static Map<String, String> parseValues(String values, String separator) {
        if(Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p-> p.getValue()));
    }

    public static Pair getKeyValue(String keyValue, String regex) {
        if(Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if(tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {
        String key;
        String value;

        public Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null)? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0: value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;
            if(getClass() != obj.getClass()) return false;

            Pair other = (Pair) obj;
            if(key == null) {
                if(other.key != null) return false;
            } else if (!key.equals(other.key)) {
                return false;
            }

            if(value == null) {
                if(other.value != null) return false;
            } else if(!value.equals(other.value)){
                return  false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }


}
