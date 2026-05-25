// AccountServiceImpl.java
package com.life.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.life.account.dto.CategoryDeltaDTO;
import com.life.account.dto.CategoryRatioDTO;
import com.life.account.dto.MonthComparisonDTO;
import com.life.account.dto.MonthlySummaryDTO;
import com.life.account.mapper.AccountMapper;
import com.life.account.vo.AccountBookVO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    @Override
    public int insert(AccountBookVO vo) {
        return accountMapper.insert(vo);
    }

    @Override
    public List<AccountBookVO> findAll(Long memberId) {
        return accountMapper.findAll(memberId);
    }

    @Override
    public List<AccountBookVO> findToday(Long memberId) {
        return accountMapper.findToday(memberId);
    }

    @Override
    public List<AccountBookVO> findByMonth(Long memberId, String month) {
        return accountMapper.findByMonth(memberId, month);
    }

    @Override
    public int delete(Long accountId) {
        return accountMapper.delete(accountId);
    }

    // 📅 캘린더
    @Override
    public Map<String, Object> getCalendar(Long memberId, String month) {

        List<AccountBookVO> list = accountMapper.findByMonth(memberId, month);
        
        System.out.println("list - " + list);

        Map<String, List<AccountBookVO>> grouped =
                list.stream()
                        .collect(Collectors.groupingBy(AccountBookVO::getCreatedAt));

        Map<String, Object> result = new HashMap<>();

        grouped.forEach((date, items) -> {

            int income = items.stream()
                    .filter(i -> "INCOME".equals(i.getType()))
                    .mapToInt(AccountBookVO::getAmount)
                    .sum();

            int expense = items.stream()
                    .filter(i -> "EXPENSE".equals(i.getType()))
                    .mapToInt(AccountBookVO::getAmount)
                    .sum();

            Map<String, Object> day = new HashMap<>();
            day.put("income", income);
            day.put("expense", expense);
            day.put("items", items);

            result.put(date, day);
        });

        return result;
    }

    // 💰 월 요약
    @Override
    public MonthlySummaryDTO getMonthlySummary(Long memberId, String month) {

        int income = accountMapper.sumIncome(memberId, month);
        int expense = accountMapper.sumExpense(memberId, month);

        MonthlySummaryDTO dto = new MonthlySummaryDTO();
        dto.setTotalIncome(income);
        dto.setTotalExpense(expense);
        dto.setBalance(income - expense);

        return dto;
    }

    // 🥧 차트
    @Override
    public List<CategoryRatioDTO> getCategoryRatio(Long memberId, String month) {

        List<CategoryRatioDTO> list = accountMapper.categorySum(memberId, month);

        int total = list.stream()
                .mapToInt(CategoryRatioDTO::getAmount)
                .sum();

        for (CategoryRatioDTO dto : list) {
            double percent = total == 0 ? 0 : (dto.getAmount() * 100.0) / total;
            dto.setPercent(Math.round(percent * 10) / 10.0);
        }

        return list;
    }

	@Override
	public MonthComparisonDTO getMonthComparison(Long memberId, String month) {
		// 이전 달 계산 (YYYY-MM)
        String prevMonth = LocalDate.parse(month + "-01")
                .minusMonths(1)
                .toString()
                .substring(0, 7);

        // 월별 총 수입 / 지출
        int currentIncome = accountMapper.sumIncome(memberId, month);
        int currentExpense = accountMapper.sumExpense(memberId, month);

        int prevIncome = accountMapper.sumIncome(memberId, prevMonth);
        int prevExpense = accountMapper.sumExpense(memberId, prevMonth);

        // 증감률 계산 (%)
        double incomeChange = prevIncome == 0 ? 0 :
                (currentIncome - prevIncome) * 100.0 / prevIncome;

        double expenseChange = prevExpense == 0 ? 0 :
                (currentExpense - prevExpense) * 100.0 / prevExpense;

        // 📊 카테고리별 데이터 조회
        List<CategoryDeltaDTO> currentList = accountMapper.currentCategorySum(memberId, month);
        List<CategoryDeltaDTO> prevList = accountMapper.prevCategorySum(memberId, prevMonth);

        /**
         *  카테고리 병합 로직
         * - 이번달/저번달 데이터 합쳐서 비교 가능하게 구성
         * - 없는 값은 0 처리
         */
        Map<String, CategoryDeltaDTO> map = new HashMap<>();

        currentList.forEach(c -> map.put(c.getCategoryName(), c));

        prevList.forEach(p -> {
            map.putIfAbsent(p.getCategoryName(), new CategoryDeltaDTO());
            map.get(p.getCategoryName()).setCategoryName(p.getCategoryName());
            map.get(p.getCategoryName()).setPrevAmount(p.getPrevAmount());
        });

        // delta 및 percent 계산
        map.values().forEach(c -> {
            int current = c.getCurrentAmount();
            int prev = c.getPrevAmount();
            c.setDelta(current - prev);
            c.setPercent(prev == 0 ? (current > 0 ? 100 : 0)
                    : (current - prev) * 100.0 / prev);
        });

        // DTO 구성
        MonthComparisonDTO dto = new MonthComparisonDTO();
        dto.setCurrentIncome(currentIncome);
        dto.setCurrentExpense(currentExpense);
        dto.setPrevIncome(prevIncome);
        dto.setPrevExpense(prevExpense);
        dto.setIncomeChange(incomeChange);
        dto.setExpenseChange(expenseChange);
        dto.setCategoryDeltas(new ArrayList<>(map.values()));

        return dto;
    }
	
}