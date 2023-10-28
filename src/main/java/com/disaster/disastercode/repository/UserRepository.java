package com.disaster.disastercode.repository;

import com.disaster.disastercode.entity.User;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * UserRepository 接口
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Repository
public interface UserRepository extends JRepository<User, Integer> {

}

