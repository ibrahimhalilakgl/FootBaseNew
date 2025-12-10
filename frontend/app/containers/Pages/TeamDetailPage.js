import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Box, Card, CardContent, CircularProgress, Typography, Alert, Stack } from '@mui/material';
import { teamsAPI } from 'utils/api';

function TeamDetailPage() {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let mounted = true;
    (async () => {
      try {
        const res = await teamsAPI.get(id);
        if (mounted) setData(res);
      } catch (e) {
        if (mounted) setError('Veri yüklenemedi. Lütfen tekrar deneyin.');
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => {
      mounted = false;
    };
  }, [id]);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !data) {
    return (
      <Box mt={2}>
        <Alert severity="error">{error || 'Kayıt bulunamadı'}</Alert>
      </Box>
    );
  }

  return (
    <Box mt={2}>
      <Typography variant="h5" gutterBottom>
        Takım Detayı
      </Typography>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={1}>
            <Typography variant="h6">{data.name || data.ad || 'Takım'}</Typography>
            <Typography color="text.secondary">Kod: {data.code || data.kod || '-'}</Typography>
            <Typography color="text.secondary">Lig: {data.league || data.lig || '-'}</Typography>
            <Typography color="text.secondary">Şehir: {data.city || data.sehir || '-'}</Typography>
            <Typography color="text.secondary">Stadyum: {data.stadium || data.stadyum || '-'}</Typography>
          </Stack>
        </CardContent>
      </Card>
      <Box mt={2}>
        <Link to="/app/teams">← Takım listesine dön</Link>
      </Box>
    </Box>
  );
}

export default TeamDetailPage;

