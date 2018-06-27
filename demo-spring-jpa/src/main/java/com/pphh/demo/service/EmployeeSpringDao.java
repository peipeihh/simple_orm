package com.pphh.demo.service;

import com.pphh.demo.dao.EmployeeDao;
import com.pphh.demo.po.EmployeeEntity;
import com.pphh.demo.repo.EmployeeRepository;
import com.pphh.demo.po.EmployeeJpaEntity;
import com.pphh.demo.util.ConvertUtils;
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
public class EmployeeSpringDao implements EmployeeDao {

    static Logger logger = LoggerFactory.getLogger(EmployeeSpringDao.class);

    @Autowired
    EmployeeRepository employeeRepo;

    public Page<EmployeeEntity> queryByPage(String field, String value, int page, int count) {
        Specification<EmployeeJpaEntity> specification = new Specification<EmployeeJpaEntity>() {
            @Override
            public Predicate toPredicate(Root<EmployeeJpaEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<String> _name = root.get(field);
                Predicate _key = criteriaBuilder.like(_name, "%" + value + "%");
                return criteriaBuilder.and(_key);
            }
        };

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, count, sort);

        Page<EmployeeJpaEntity> employeeJpaEntities = employeeRepo.findAll(specification, pageable);
        return employeeJpaEntities.map(new EmployeeConverter());
    }

    @Override
    public EmployeeEntity selectById(long id) {
        EmployeeJpaEntity employeeJpaEntity = employeeRepo.findOne(id);
        return ConvertUtils.convert(employeeJpaEntity, EmployeeEntity.class);
    }

    @Override
    public List<EmployeeEntity> selectAll() {
        List<EmployeeJpaEntity> employeeJpaEntities = employeeRepo.findAll();
        return ConvertUtils.convert(employeeJpaEntities, EmployeeEntity.class);
    }

    @Override
    public EmployeeEntity selectLast() {
        EmployeeJpaEntity employeeJpaEntity = employeeRepo.findTopByOrderByIdDesc();
        return ConvertUtils.convert(employeeJpaEntity, EmployeeEntity.class);
    }

    @Override
    public long count() {
        return employeeRepo.count();
    }

    @Override
    public long insert(EmployeeEntity employee) {
        long newEmployeeId = 0;

        EmployeeJpaEntity employeeJpaEntity = ConvertUtils.convert(employee, EmployeeJpaEntity.class);
        employeeJpaEntity = employeeRepo.save(employeeJpaEntity);
        if (employeeJpaEntity != null) {
            newEmployeeId = employeeJpaEntity.getId();
            employee.setId(newEmployeeId);
        }

        return newEmployeeId;
    }

    @Override
    public boolean update(EmployeeEntity employee) {
        EmployeeJpaEntity employeeJpaEntity = ConvertUtils.convert(employee, EmployeeJpaEntity.class);
        employeeJpaEntity = employeeRepo.save(employeeJpaEntity);
        return employeeJpaEntity != null;
    }

    @Override
    public boolean delete(EmployeeEntity employee) {
        EmployeeJpaEntity employeeJpaEntity = ConvertUtils.convert(employee, EmployeeJpaEntity.class);
        employeeRepo.delete(employeeJpaEntity);
        return true;
    }

    @Override
    public boolean deleteById(long id) {
        employeeRepo.delete(id);
        return false;
    }
}
