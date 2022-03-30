package com.kk.application.playerinfo.repos;

import com.kk.application.playerinfo.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PlayerRepository extends PagingAndSortingRepository<PlayerRecordEntity,Long> {

    public Page<PlayerRecordEntity> findByName(String name, Pageable pageable);
    public List<PlayerRecordEntity> findByName(String name);
    public Page<PlayerRecordEntity> findByPlayerId(long id, Pageable paging);
    List<PlayerRecordEntity> findByPlayerId(long id);
}
