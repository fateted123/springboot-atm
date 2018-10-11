package com.dayuanit.dy9.springboot.atm.springbootatm.mapper;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Transfer;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Transfer record);

    int insertSelective(Transfer record);

    Transfer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Transfer record);

    int updateByPrimaryKey(Transfer record);

    List<Transfer> listTransfer(@Param("status") Integer status, @Param("deadTime") Date deadTime,
                                @Param("offset") Integer offset, @Param("prePageNum") Integer prePageNum);

    int modifyTransferStatus(@Param("id") Integer id, @Param("newStatus") Integer newStatus);
}