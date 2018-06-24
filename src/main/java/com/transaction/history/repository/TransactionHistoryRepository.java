package com.transaction.history.repository;

import com.transaction.history.exception.TransactionHistoryException;
import com.transaction.history.model.Transaction;
import com.transaction.history.model.TransactionStatstics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class TransactionHistoryRepository {
    private static final Logger logger = Logger.getLogger(TransactionHistoryRepository.class.getName());

    public static final String INSERT_QUERY = "insert into transactions (id, amount, transaction_time) values (?,  ?, ?) ";
    public static final String STATS_SELECT_QUERY = "select sum(amount) sumOfAmount,max(amount) maximumAmount," +
                                                     "min(amount) minimumAmount, count(amount) noOfTransactions " +
                                                     "from transactions where transaction_time > ?";

    @Value("${transactions.within.time}")
    private int transactionsWithInTime;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Transaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        Timestamp timeStamp = new Timestamp(transaction.getTransactionTime());
        return jdbcTemplate.update(INSERT_QUERY,
                transaction.getId(), transaction.getAmount(), timeStamp);
    }

    public TransactionStatstics getStatstics() throws TransactionHistoryException {

        try {
            RowMapper statsticsMapper = getRowMapper();
            Timestamp timeLimitTimeStamp = getTimeLimit();
            Object[] parameters = new Object[] {timeLimitTimeStamp};
            return (TransactionStatstics) jdbcTemplate.queryForObject(STATS_SELECT_QUERY, parameters ,
                    new int[]{Types.TIMESTAMP}, statsticsMapper);
        } catch (SQLException e) {
            logger.severe("TransactionHistoryRepository :: getStatstics, SQL exception occured");
            throw new TransactionHistoryException("Could not get the statstics");
        }
    }


    private Timestamp getTimeLimit() {
        return new Timestamp(new Date().getTime() - (transactionsWithInTime * 1000));
    }

    private RowMapper   getRowMapper() throws SQLException {
        return new RowMapper<TransactionStatstics>() {
            @Override
            public TransactionStatstics mapRow(ResultSet rs, int rowNum) throws SQLException {
                TransactionStatstics transactionStatstics = new TransactionStatstics();
                transactionStatstics.setSumOfAmount(rs.getDouble("sumOfAmount"));
                transactionStatstics.setMaximumAmount(rs.getDouble("maximumAmount"));
                transactionStatstics.setMinimumAmount(rs.getDouble("minimumAmount"));
                transactionStatstics.setNoOfTransactions(rs.getLong("noOfTransactions"));
                if(transactionStatstics.getNoOfTransactions() > 0) {
                    transactionStatstics.setAverageOfAmount(transactionStatstics.getSumOfAmount() / transactionStatstics.getNoOfTransactions());
                }else {
                    transactionStatstics.setAverageOfAmount(0.0);
                }
                return transactionStatstics;
            }
        };
    }


}
