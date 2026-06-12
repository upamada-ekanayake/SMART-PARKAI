import { createContext, useContext, useMemo, useState } from 'react';
import { api, unwrap } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('parksmart_user');
    return raw ? JSON.parse(raw) : null;
  });

  const login = async (email, password) => {
    const data = unwrap(await api.post('/auth/login', { email, password }));
    localStorage.setItem('parksmart_token', data.token);
    localStorage.setItem('parksmart_user', JSON.stringify(data));
    setUser(data);
    return data;
  };

  const register = async (payload) => {
    const data = unwrap(await api.post('/auth/register', payload));
    localStorage.setItem('parksmart_token', data.token);
    localStorage.setItem('parksmart_user', JSON.stringify(data));
    setUser(data);
    return data;
  };

  const logout = () => {
    localStorage.removeItem('parksmart_token');
    localStorage.removeItem('parksmart_user');
    setUser(null);
  };

  const value = useMemo(() => ({ user, login, register, logout, isAdmin: user?.role === 'ROLE_ADMIN' }), [user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
