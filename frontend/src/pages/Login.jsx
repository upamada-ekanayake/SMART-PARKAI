import { Alert, Box, Button, Paper, Stack, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [form, setForm] = useState({ email: 'admin@parksmart.ai', password: 'admin123' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(form.email, form.password);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'grid', placeItems: 'center', p: 2, bgcolor: 'background.default' }}>
      <Paper component="form" onSubmit={submit} sx={{ width: '100%', maxWidth: 420, p: 4 }}>
        <Stack spacing={2.5}>
          <Box>
            <Typography variant="h4">ParkSmart AI</Typography>
            <Typography color="text.secondary">Sign in to manage parking operations.</Typography>
          </Box>
          {error && <Alert severity="error">{error}</Alert>}
          <TextField label="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} fullWidth />
          <TextField label="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} fullWidth />
          <Button type="submit" variant="contained" size="large">Login</Button>
          <Typography variant="body2">New user? <Link to="/register">Create an account</Link></Typography>
        </Stack>
      </Paper>
    </Box>
  );
}
