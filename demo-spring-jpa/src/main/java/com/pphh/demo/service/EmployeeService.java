package com.pphh.demo.service;

import com.pphh.demo.dao.EmployeeRepository;
import com.pphh.demo.po.EmployeeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 6/24/2018
 */
@Service
public class EmployeeService {

    static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    EmployeeRepository employeeDao;

    public EmployeeEntity queryById(Long id) {
        EmployeeEntity employee = employeeDao.findOne(id);
        printEmployeeInfo(employee);
        return employee;
    }

    public Page<EmployeeEntity> queryByPage(String field, String value, int page, int count) {

        Specification<EmployeeEntity> specification = new Specification<EmployeeEntity>() {
            @Override
            public Predicate toPredicate(Root<EmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Path<String> _name = root.get(field);
                Predicate _key = criteriaBuilder.like(_name, "%" + value + "%");
                return criteriaBuilder.and(_key);

            }
        };

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, count, sort);

        return employeeDao.findAll(specification, pageable);
    }

    private void printEmployeeList(List<EmployeeEntity> employees) {
        if (employees != null) {
            for (int i = 0; i < employees.size(); i++) {
                if (i == 0) {
                    logger.info("id - first name, last name, birth date, employed, occupation, insert by, insert at, update by, update at");
                }

                printEmployeeInfo(employees.get(i));
            }
        }
    }

    private void printEmployeeInfo(EmployeeEntity employee) {

        if (employee != null) {
            String msg = String.format("%s - %s, %s, %s, %s, %s, %s, %s, %s, %s",
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getBirthDate(),
                    employee.getEmployed(),
                    employee.getOccupation(),
                    employee.getInsertBy(),
                    employee.getInsertTime(),
                    employee.getUpdateBy(),
                    employee.getUpdateTime());
            logger.info(msg);
        }

    }
}
