import { Box, Card, CardContent, Grid, Stack, Typography } from '@mui/material';
import { useEffect, useState } from 'react';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { api, unwrap } from '../api/client';

export default function Dashboard() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    api.get('/admin/dashboard').then((res) => setStats(unwrap(res))).catch(() => {
      setStats({ totalUsers: 0, totalVehicles: 0, totalParkingLots: 0, totalSlots: 0, totalBookings: 0, revenue: 0, occupancyRate: 0 });
    });
  }, []);

  const cards = [
    ['Users', stats?.totalUsers],
    ['Vehicles', stats?.totalVehicles],
    ['Lots', stats?.totalParkingLots],
    ['Slots', stats?.totalSlots],
    ['Bookings', stats?.totalBookings],
    ['Revenue', `Rs. ${stats?.revenue || 0}`],
    ['Occupancy', `${stats?.occupancyRate || 0}%`]
  ];
  const chart = cards.slice(0, 5).map(([name, value]) => ({ name, value: Number(value || 0) }));

  return (
    <Stack spacing={3}>
      <Box>
        <Typography variant="h4">Dashboard</Typography>
        <Typography color="text.secondary">Live operating metrics for smart parking teams.</Typography>
      </Box>
      <Grid container spacing={2}>
        {cards.map(([label, value]) => (
          <Grid item xs={12} sm={6} md={3} key={label}>
            <Card><CardContent><Typography color="text.secondary">{label}</Typography><Typography variant="h5">{value ?? '-'}</Typography></CardContent></Card>
          </Grid>
        ))}
      </Grid>
      <Card>
        <CardContent sx={{ height: 320 }}>
          <Typography variant="h6" sx={{ mb: 2 }}>System Volume</Typography>
          <ResponsiveContainer width="100%" height="85%">
            <BarChart data={chart}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="name" /><YAxis allowDecimals={false} /><Tooltip /><Bar dataKey="value" fill="#0f766e" radius={[4, 4, 0, 0]} /></BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
    </Stack>
  );
}
