package com.hxb.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class AccountDaoImpl implements AccountDao {

    //声明jdbcTemplate属性及setter方法
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //添加
    @Override
    public int addAccount(Account account) {
        String sql = "insert into account(username,balance) values(?,?)";
        Object[] objects = new Object[]{
                account.getUsername(),
                account.getBalance()
        };
        //执行添加操作
        int num = this.jdbcTemplate.update(sql, objects);
        return num;
    }

    //更新
    @Override
    public int updateAccount(Account account) {
        String sql ="update account set username=?,balance=? where id=?";
        Object [] objects = new Object[]{
                account.getUsername(),
                account.getBalance(),
                account.getId()
        };
        int num=  this.jdbcTemplate.update(sql,objects);
        return num;
    }

    //删除
    @Override
    public int deleteAccount(int id) {
         String sql = "delete from account where id = ?";
         int num=  this.jdbcTemplate.update(sql,id);
        return num;
    }

    @Override
    public Account findAccountById(int id) {
        //定义SQL语句
        String sql = "select * from account where id = ?";
        // 创建一个新的BeanPropertyRowMapper对象
        RowMapper<Account> rowMapper =
                new BeanPropertyRowMapper<Account>(Account.class);
        // 将id绑定到SQL语句中，并通过RowMapper返回一个Object类型的单行记录
        return this.jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Account> findAllAccount() {
        // 定义SQL语句
        String sql = "select * from account";
        // 创建一个新的BeanPropertyRowMapper对象
        RowMapper<Account> rowMapper =
                new BeanPropertyRowMapper<Account>(Account.class);
        // 执行静态的SQL查询，并通过RowMapper返回结果
        return this.jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void transfer(String outUser, String inUser, Double money) {
        //收款时,收款用户余额为=现有金额+所汇金额
        this.jdbcTemplate.update("update account set balance = balance+? where username=?",money,inUser);
        //汇款时,汇款用户金额=现有金额-所汇 金额
        this.jdbcTemplate.update("update account set balance = balance -? where username = ?",money,outUser);
    }
}
