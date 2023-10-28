package com.disaster.disastercode.repository;

import com.disaster.disastercode.entity.Operlog;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * OperlogRepository 接口
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Repository
public interface OperlogRepository extends JRepository<Operlog, Integer> {

}

