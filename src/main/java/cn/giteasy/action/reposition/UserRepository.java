package cn.giteasy.action.reposition;

import cn.giteasy.action.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Axin in 2019/12/16 20:02
 *
 * 不用写实现类，JPA已经有实现类了
 */
public interface UserRepository extends CrudRepository<User,Long> {


}
