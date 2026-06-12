import { Button, Card, CardContent, Grid, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from '@mui/material';
import Queue from '@mui/icons-material/Queue';
import Refresh from '@mui/icons-material/Refresh';
import { useEffect, useState } from 'react';
import { api, unwrap } from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function WaitingQueue() {
  const { user } = useAuth();
  const [parkingLotId, setParkingLotId] = useState('');
  const [rows, setRows] = useState([]);
  const load = async () => setRows(unwrap(await api.get('/waiting-queue')) || []);
  useEffect(() => { load().catch(() => setRows([])); }, []);
  const join = async () => {
    await api.post('/waiting-queue/join', { userId: user.id, parkingLotId: Number(parkingLotId) });
    setParkingLotId('');
    await load();
  };
  return (
    <Stack spacing={3}>
      <Grid container alignItems="center" spacing={2}>
        <Grid item xs><Typography variant="h4">Waiting Queue</Typography><Typography color="text.secondary">Join a lot queue when no slots are available.</Typography></Grid>
        <Grid item><Button startIcon={<Refresh />} onClick={load}>Refresh</Button></Grid>
      </Grid>
      <Card><CardContent><Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}><TextField label="Parking Lot ID" type="number" value={parkingLotId} onChange={(e) => setParkingLotId(e.target.value)} /><Button variant="contained" startIcon={<Queue />} onClick={join}>Join Queue</Button></Stack></CardContent></Card>
      <Card><Table><TableHead><TableRow><TableCell>ID</TableCell><TableCell>User</TableCell><TableCell>Lot</TableCell><TableCell>Joined</TableCell><TableCell>Notified</TableCell></TableRow></TableHead><TableBody>{rows.map((row) => <TableRow key={row.id}><TableCell>{row.id}</TableCell><TableCell>{row.userName}</TableCell><TableCell>{row.parkingLotName}</TableCell><TableCell>{row.joinedAt}</TableCell><TableCell>{String(row.notified)}</TableCell></TableRow>)}</TableBody></Table></Card>
    </Stack>
  );
}
