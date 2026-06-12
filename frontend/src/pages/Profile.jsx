import { Card, CardContent, Stack, Typography } from '@mui/material';
import { useAuth } from '../context/AuthContext';

export default function Profile() {
  const { user } = useAuth();
  return (
    <Stack spacing={3}>
      <Typography variant="h4">Profile</Typography>
      <Card><CardContent><Typography variant="h6">{user.firstName} {user.lastName}</Typography><Typography>{user.email}</Typography><Typography color="text.secondary">{user.role} {user.vip ? 'VIP' : ''}</Typography></CardContent></Card>
    </Stack>
  );
}
