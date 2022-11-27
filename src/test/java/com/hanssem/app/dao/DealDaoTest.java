package com.hanssem.app.dao;

import com.hanssem.app.dto.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class DealDaoTest {
    @Autowired
    DealDao DealDao;
    @Test
    public void select() throws Exception{
        ProductDto dto = DealDao.select(1);
        System.out.println("dealDto : " + dto);

    }
}