import { createContext, useState, useEffect, type ReactNode } from 'react';
import { authService } from '../api/auth/authService';
import type { UserResponse, LoginRequest, RegisterRequest } from '../api/auth/types';

interface AuthContextType {
  user: UserResponse | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (loginRequest: LoginRequest) => Promise<void>;
  register: (registerRequest: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;

    const checkActiveSession = async () => {
      try {
        const currentUser = await authService.getCurrentUser();
        if (isMounted) {
          setUser(currentUser);
        }
      } catch (error) {
        if (isMounted) {
          setUser(null);
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    };

    checkActiveSession();

    return () => {
      isMounted = false;
    };
  }, []);

  const login = async (loginRequest: LoginRequest) => {
    try {
      const currentUser = await authService.login(loginRequest);
      setUser(currentUser);
    } catch (error) {
      setUser(null);
      throw error;
    }
  };

  const register = async (registerRequest: RegisterRequest) => {
    try {
      await authService.register(registerRequest);
    } catch (error) {
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Failed to log out from the server:', error);
    } finally {
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
