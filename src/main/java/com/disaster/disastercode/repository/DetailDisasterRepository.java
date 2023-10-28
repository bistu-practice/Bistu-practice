package com.disaster.disastercode.repository;

import com.disaster.disastercode.entity.DetailDisaster;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * DetailDisasterRepository 接口
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Repository
public interface DetailDisasterRepository extends JRepository<DetailDisaster, Integer> {

}

