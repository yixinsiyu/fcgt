package net.engining.gateway.fts.invoker.sccc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.Invoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.control.core.invoker.Skippable;
import net.engining.gateway.fts.sao.sccc.AccountingSao;
import net.engining.gateway.fts.sdk.bean.EveryDayAccountingBean;
import net.engining.gateway.fts.sdk.bean.loan.LoanAccountRequestData;
import net.engining.gateway.fts.sdk.bean.resultBack.ResponseData;
import net.engining.gateway.fts.sdk.key.DataCheckResultKey;
import net.engining.gateway.fts.sdk.key.EveryDayAccountingBeanKey;
import net.engining.gateway.fts.sdk.key.LoanAccountRequestDataKey;
import net.engining.gateway.fts.sdk.key.ResponseDataKey;
import net.engining.pg.web.WebCommonResponse;

/**
 * TODO 以下逻辑只是例子，待实现
 * @author luxue
 *
 */
@InvokerDefinition(
		name = "贷款发放记账", 
		requires = { 
				}, 
		results = { 
				DataCheckResultKey.class 
				}
		)
public class LoanAccountPostTxn implements Invoker, Skippable {
	
	private static final Logger log = LoggerFactory.getLogger(LoanAccountPostTxn.class);
	
	@Autowired
	AccountingSao outerLedgerTxnHstSao;

	@Override
	public void invoke(FlowContext ctx) {
		
		EveryDayAccountingBean loanAccountBean = ctx.get(EveryDayAccountingBeanKey.class);
		//TODO
		//调用微服务
		ResponseData response = outerLedgerTxnHstSao.loanAccountTxn(loanAccountBean);
		
		ctx.put(ResponseDataKey.class, response);
		String result = JSON.toJSONString(response);
		log.info("贷款发放的结果为："  + result);
	}

	/* (non-Javadoc)
	 * @see net.engining.control.core.invoker.Skippable#skippable(java.util.Map)
	 */
	@Override
	public boolean skippable(Map<String, String> parameters) {
		String skip = parameters.get(FlowContext.CONS_PARAMETERS.SKIP.toString());
		if("true".equals(skip)){
			return true;
		}
		if("Yes".equals(skip)){
			return true;
		}
		return false;
	}

}
