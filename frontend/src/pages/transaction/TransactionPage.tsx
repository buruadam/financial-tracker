import { useEffect, useState } from 'react';
import { RefreshCw, ArrowDownRight, ArrowUpRight } from 'lucide-react';
import { transactionService } from '../../api/transaction/transactionService';
import type { TransactionResponse } from '../../api/transaction/types';

type FilterType = 'ALL' | 'INCOME' | 'EXPENSE';

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState<TransactionResponse[]>([]);
  const [activeFilter, setActiveFilter] = useState<FilterType>('ALL');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const loadTransactions = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await transactionService.getAllTransactions();
      setTransactions(data);
    } catch (err: any) {
      setTransactions([]);
      setError('Failed to load transactions. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTransactions();
  }, []);

  const formatCurrency = (amount: number) => {
    return amount.toLocaleString('hu-HU', { minimumFractionDigits: 2 }) + ' Ft';
  };

  const filteredTransactions = transactions.filter((t) => {
    if (activeFilter === 'ALL') return true;
    return t.transactionType === activeFilter;
  });

  return (
    <div className="space-y-6 max-w-4xl">
      <div className="flex justify-between items-center border-b border-zinc-100 pb-5">
        <div>
          <h1 className="text-3xl font-bold text-zinc-950 tracking-tight">Transactions</h1>
          <p className="text-zinc-500 mt-1">Manage and track your financial movements</p>
        </div>

        <button
          onClick={loadTransactions}
          disabled={loading}
          title="Refresh List"
          className="p-3 text-zinc-500 hover:text-zinc-950 hover:bg-zinc-50 disabled:text-zinc-300 bg-white border border-zinc-200 shadow-sm rounded-2xl transition-all cursor-pointer disabled:cursor-not-allowed flex items-center justify-center"
        >
          <RefreshCw size={20} className={`${loading ? 'animate-spin' : ''}`} />
        </button>
      </div>

      {!error && transactions.length > 0 && (
        <div className="flex gap-1.5 p-1 bg-zinc-100 rounded-xl w-fit">
          <button
            onClick={() => setActiveFilter('ALL')}
            className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all cursor-pointer ${activeFilter === 'ALL'
              ? 'bg-white text-zinc-950 shadow-sm'
              : 'text-zinc-500 hover:text-zinc-900'
              }`}
          >
            All ({transactions.length})
          </button>
          <button
            onClick={() => setActiveFilter('INCOME')}
            className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all cursor-pointer ${activeFilter === 'INCOME'
              ? 'bg-white text-emerald-600 shadow-sm'
              : 'text-zinc-500 hover:text-emerald-600'
              }`}
          >
            Income ({transactions.filter(t => t.transactionType === 'INCOME').length})
          </button>
          <button
            onClick={() => setActiveFilter('EXPENSE')}
            className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all cursor-pointer ${activeFilter === 'EXPENSE'
              ? 'bg-white text-red-500 shadow-sm'
              : 'text-zinc-500 hover:text-red-500'
              }`}
          >
            Expenses ({transactions.filter(t => t.transactionType === 'EXPENSE').length})
          </button>
        </div>
      )}

      {error && (
        <div className="p-4 bg-red-50 border border-red-200 text-red-600 rounded-2xl text-sm flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
          {error}
        </div>
      )}

      {loading && transactions.length === 0 && (
        <div className="flex justify-center py-12">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-zinc-950 border-t-transparent"></div>
        </div>
      )}

      {!loading && transactions.length === 0 && !error && (
        <div className="text-center py-12 bg-white rounded-2xl border border-zinc-200 shadow-sm">
          <p className="text-zinc-500 text-sm">No transactions found in your account.</p>
        </div>
      )}

      {!loading && filteredTransactions.length === 0 && !error && transactions.length > 0 && (
        <div className="text-center py-8 bg-zinc-50 rounded-2xl border border-dashed border-zinc-200">
          <p className="text-zinc-400 text-sm font-medium">No transactions match the selected filter.</p>
        </div>
      )}

      <div className="space-y-3">
        {filteredTransactions.map((t) => {
          const isIncome = t.transactionType === 'INCOME';
          return (
            <div
              key={t.id}
              className="p-4 bg-white rounded-2xl border border-zinc-200 flex justify-between items-center hover:shadow-md hover:border-zinc-300 transition-all gap-4"
            >
              <div className="min-w-0 flex items-center gap-4">
                <div className={`w-11 h-11 rounded-xl flex items-center justify-center flex-shrink-0 ${isIncome ? 'bg-emerald-50 text-emerald-600' : 'bg-red-50 text-red-500'
                  }`}>
                  {isIncome ? <ArrowUpRight size={20} /> : <ArrowDownRight size={20} />}
                </div>

                <div className="min-w-0">
                  <p className="font-semibold text-zinc-950 truncate">
                    {t.description || 'No description'}
                  </p>

                  <div className="text-xs text-zinc-400 mt-1 flex flex-wrap gap-2 items-center">
                    <span className="font-medium text-zinc-500">{t.date}</span>
                    <span>•</span>
                    <span className="bg-zinc-50 border border-zinc-100 text-zinc-700 px-2 py-0.5 rounded-lg font-medium text-[11px]">
                      {t.accountName}
                    </span>
                    <span>•</span>
                    <span className="bg-zinc-50 text-zinc-600 border border-zinc-100 px-2 py-0.5 rounded-lg text-[11px]">
                      {t.categoryName}
                    </span>
                  </div>
                </div>
              </div>

              <span className={`text-base font-bold tracking-tight whitespace-nowrap pl-2 ${isIncome ? 'text-emerald-600' : 'text-red-500'
                }`}>
                {isIncome ? '+' : '-'}&nbsp;{formatCurrency(t.amount)}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
