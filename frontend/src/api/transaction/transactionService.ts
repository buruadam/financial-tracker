import apiClient from '../apiClient';
import type { TransactionResponse } from './types';

export const transactionService = {
  getAllTransactions: async (): Promise<TransactionResponse[]> => {
    const response = await apiClient.get<TransactionResponse[]>('/transactions');
    return response.data;
  }
};
