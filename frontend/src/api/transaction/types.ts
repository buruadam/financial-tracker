export interface TransactionResponse {
  id: string;
  amount: number;
  description: string;
  date: string;
  accountId: string;
  accountName: string;
  categoryId: string;
  categoryName: string;
  transactionType: 'INCOME' | 'EXPENSE';
}
