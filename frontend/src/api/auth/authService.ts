import apiClient from '../apiClient';
import type { LoginRequest, RegisterRequest, UserResponse } from './types';

export const authService = {
  register: async (registerRequest: RegisterRequest): Promise<UserResponse> => {
    const response = await apiClient.post<UserResponse>('/auth/register', registerRequest);
    return response.data;
  },

  login: async (loginRequest: LoginRequest): Promise<UserResponse> => {
    const response = await apiClient.post<UserResponse>('/auth/login', loginRequest);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/auth/logout');
  },

  getCurrentUser: async (): Promise<UserResponse> => {
    const response = await apiClient.get<UserResponse>('/auth/me');
    return response.data;
  }
};
