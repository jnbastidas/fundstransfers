package com.yellowpepper.fundstransfers.service;

import javax.transaction.SystemException;

import com.yellowpepper.fundstransfers.dto.TransferInputDTO;
import com.yellowpepper.fundstransfers.dto.TransferOutputDTO;


public interface TransferService {
	TransferOutputDTO send(final TransferInputDTO transfer) throws SystemException;
}
