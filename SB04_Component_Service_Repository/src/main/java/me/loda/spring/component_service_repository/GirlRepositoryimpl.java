package me.loda.spring.component_service_repository;

import org.springframework.stereotype.Repository;

@Repository
public class GirlRepositoryimpl implements  GirlRepository{

    @Override
    public Girl getGirlByName(String name) {
        return new Girl(name);
    }
}
