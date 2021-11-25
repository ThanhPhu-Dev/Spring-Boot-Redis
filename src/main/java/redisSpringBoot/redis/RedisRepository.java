package redisSpringBoot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Component
public class RedisRepository implements CommandLineRunner {

    @Autowired
    private RedisTemplate template;

    @Override
    public void run(String... args) throws Exception {
        Product pro = new Product(1,"redis", 2,2000);
        Product pro2 = new Product(2,"redis2", 5,8000);
        //lưu product
        create("product",pro);
        create("product",pro2);
        // In ra màn hình Giá trị của key trong Redis
        System.out.println("giá trị của key: "+ getValueKey("product", 1));

        //get entries
        Map<String, Product> mapPro = getAll("product");
        Iterator<String> itr = mapPro.keySet().iterator();
        System.out.println("Danh sách: ");
        while(itr.hasNext()){
            System.out.println(mapPro.get(itr.next()).toString());
        }

        //update
        pro.setName("redisUpdate");
        update("product", 1, pro);
        System.out.println("Cập nhật "+(Product)template.opsForHash().get("product", pro.getId()));

        //remove
        remove("product", 1);
        System.out.println("Kich thuoc sau khi xoa product id = 1: "+ getSize("product"));
    }

    public Long getSize(String hashKey){
        return template.opsForHash().size(hashKey);
    }

    public void create(String hashKey, Product pro){
        template.opsForHash().put(hashKey, pro.getId(), pro);
    }

    public Product getValueKey(String hashKey, int key){
        return  (Product)template.opsForHash().get(hashKey, key);
    }

    public Map<String,Product> getAll(String hashKey){
        return  (Map)template.opsForHash().entries(hashKey);
    }

    public void update(String hashKey,int key,Product pro){
        template.opsForHash().put(hashKey, key, pro);
    }

    public void remove(String hashKey, int key){
        template.opsForHash().delete("product", key);
    }
}
