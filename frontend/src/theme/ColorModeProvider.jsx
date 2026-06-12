import { createContext, useContext, useMemo, useState } from 'react';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';

const ColorModeContext = createContext(null);

export function ColorModeProvider({ children }) {
  const [mode, setMode] = useState(() => localStorage.getItem('parksmart_theme') || 'light');
  const toggleMode = () => {
    setMode((current) => {
      const next = current === 'light' ? 'dark' : 'light';
      localStorage.setItem('parksmart_theme', next);
      return next;
    });
  };

  const theme = useMemo(() => createTheme({
    palette: {
      mode,
      primary: { main: '#0f766e' },
      secondary: { main: '#f59e0b' },
      background: {
        default: mode === 'light' ? '#f6f7f9' : '#111827',
        paper: mode === 'light' ? '#ffffff' : '#1f2937'
      }
    },
    shape: { borderRadius: 8 },
    typography: {
      fontFamily: 'Inter, Roboto, Arial, sans-serif',
      h4: { fontWeight: 800 },
      h6: { fontWeight: 700 }
    },
    components: {
      MuiButton: { styleOverrides: { root: { textTransform: 'none', fontWeight: 700 } } },
      MuiCard: { styleOverrides: { root: { borderRadius: 8 } } }
    }
  }), [mode]);

  return (
    <ColorModeContext.Provider value={{ mode, toggleMode }}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}

export const useColorMode = () => useContext(ColorModeContext);
