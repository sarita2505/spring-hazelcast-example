package com.spring.dao.impl;

import com.spring.configuration.HazelCastConfig;
import com.spring.constants.AppConstants;
import com.spring.model.Employee;
import com.spring.utils.EmployeeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class EmployeeDtaImpl extends AbstractDataDaoImpl<Employee, Integer> {
    private static   Logger logger= LoggerFactory.getLogger(EmployeeDtaImpl.class);
    String save = "insert into employee(name) values(?)";
    String update = "update employee set name=? where id=?";
    String delete = "delete from employee where id=?";
    String findall = "select * from employee";
    String findById = "select * from employee where id=?";
    @Autowired
    HazelCastConfig hazelCastConfig;

    private PreparedStatementCreator getPsc(Employee e) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, e.getName());
                return ps;
            }
        };
        return psc;
    }

    @Override
    public Integer save(Employee model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = 0;
        rows = getTemplate().update(save, new Object[]{model.getName()});

//        List<Employee> employees = hazelCastConfig.getList(AppConstants.HC_EMPLOYEE_LIST);
//        System.out.println("data in hazelcast list" + employees);
//        rows =getTemplate().update(getPsc(model),keyHolder);
//        int id = keyHolder.getKey().intValue();
//        model.setId(id);
       // employees.add(model);
        return rows;
    }

    @Override
    public Employee update(Employee model, Integer id) {
        getTemplate().update(update, new Object[]{model.getName(), id});
        return findById(id);
    }

    @Override
    public Integer delete(Integer id) {
        return getTemplate().update(delete, new Object[]{id});
    }

    @Override
    public List<Employee> find() {
        List<Employee>employees= getTemplate().query(findall, new EmployeeRowMapper());
        List<Employee> employees1 = hazelCastConfig.getList(AppConstants.HC_EMPLOYEE_LIST);
        for (Employee employee:employees) {
            if(!employees1.contains(employee)){
            employees1.add(employee);}
        }
        logger.info("data in hazelcast list" + employees1);
        System.out.println("data in hazelcast list" + employees1);
        return employees;
    }

    @Override
    public Employee findById(Integer id) {
        Employee employee = new Employee();
        employee = getTemplate().queryForObject(findById, new EmployeeRowMapper(), new Object[]{id});
        return employee;

    }
}
