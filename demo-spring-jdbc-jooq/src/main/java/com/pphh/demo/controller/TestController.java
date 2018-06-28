package com.pphh.demo.controller;

import com.pphh.demo.DaoTester;
import com.pphh.demo.dao.EmployeeJdbcDao;
import com.pphh.demo.dao.EmployeeJdbcJooqDao;
import com.pphh.demo.jooq.db.mysql.tables.Employee;
import com.pphh.demo.po.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 6/28/2018
 */
@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeJdbcJooqDao employeeJdbcJooqDao;

    @RequestMapping("/test")
    public String test() {
        DaoTester daoTester = DaoTester.getInstance();
        daoTester.run(employeeJdbcJooqDao);
        return "spring jdbc + jooq dao test is completed, please check output log.";
    }

    @RequestMapping("/selectById")
    public EmployeeEntity selectById(@RequestParam(name = "id") Long id) {
        return employeeJdbcJooqDao.selectById(id);
    }

    @RequestMapping("/selectAll")
    public List<EmployeeEntity> selectAll() {
        return employeeJdbcJooqDao.selectAll();
    }

    @RequestMapping("/selectLast")
    public EmployeeEntity selectLast() {
        return employeeJdbcJooqDao.selectLast();
    }

    @RequestMapping("/count")
    public Long count() {
        return employeeJdbcJooqDao.count();
    }

    @RequestMapping("/update")
    public String update() {
        EmployeeEntity employee = employeeJdbcJooqDao.selectLast();
        String firstName = "test-" + System.currentTimeMillis();
        employee.setFirstName(firstName);
        Boolean bSuccess = employeeJdbcJooqDao.update(employee);
        return String.format("Update employee, id = %s, first name = %s, bSuccess = %s", employee.getId(), firstName, bSuccess);
    }

    @RequestMapping("/insertNew")
    public Long insertNew() {
        EmployeeEntity employeeEntity = EmployeeEntity.createOne();
        return employeeJdbcJooqDao.insert(employeeEntity);
    }

    @RequestMapping("/deleteById")
    public Boolean deleteById(@RequestParam(name = "id") Long id) {
        return employeeJdbcJooqDao.deleteById(id);
    }

}