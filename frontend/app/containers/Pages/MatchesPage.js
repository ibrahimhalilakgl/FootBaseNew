import React, { useState, useEffect, useMemo } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Box,
  Chip,
  Stack,
  Divider,
  Paper,
} from '@mui/material';
import SportsSoccerIcon from '@mui/icons-material/SportsSoccer';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import PlaceIcon from '@mui/icons-material/Place';
import StadiumIcon from '@mui/icons-material/Stadium';
import { matchesAPI } from 'utils/api';
import PapperBlock from 'dan-components/PapperBlock/PapperBlock';

const statusLabels = [
  { key: null, label: 'Tümü' },
  { key: 'PLANLI', label: 'Planlı' },
  { key: 'FINISHED', label: 'Bitti' },
  { key: 'BITTI', label: 'Bitti' },
  { key: 'SCHEDULED', label: 'Takvim' },
];

function MatchesPage() {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState(null);
  const [statusFilter, setStatusFilter] = useState(null);

  useEffect(() => {
    loadMatches();
  }, []);

  const loadMatches = async () => {
    try {
      setLoading(true);
      const data = await matchesAPI.list();
      setMatches(Array.isArray(data) ? data : []);
      setError(null);
    } catch (e) {
      console.error('Maçlar yüklenemedi:', e);
      setError('Maç listesi alınamadı. Lütfen daha sonra tekrar deneyin.');
    } finally {
      setLoading(false);
    }
  };

  const filteredMatches = useMemo(() => (
    matches.filter((match) => {
      const term = searchTerm.toLowerCase();
      const statusOk = !statusFilter || (match.status || '').toUpperCase() === statusFilter;
      return statusOk && (
        (match.homeTeam && match.homeTeam.toLowerCase().includes(term)) ||
        (match.awayTeam && match.awayTeam.toLowerCase().includes(term))
      );
    })
  ), [matches, searchTerm, statusFilter]);

  const hero = (
    <Paper
      elevation={3}
      sx={{
        p: { xs: 2, md: 4 },
        mb: 3,
        borderRadius: 3,
        background: 'linear-gradient(135deg, #0d2b45 0%, #14746f 100%)',
        color: '#fff',
      }}
    >
      <Stack spacing={2}>
        <Stack direction="row" spacing={1} alignItems="center">
          <StadiumIcon />
          <Typography variant="h5" fontWeight="bold">Haftanın Maçları</Typography>
        </Stack>
        <Typography variant="body1" sx={{ maxWidth: 720 }}>
          Program, canlı skor ve taraftar yorumları tek ekranda. Takımını ara, biten maçları incele,
          yaklaşan maçları kaçırma.
        </Typography>
        <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
          <TextField
            fullWidth
            label="Takım veya maç ara"
            variant="outlined"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Örneğin: Galatasaray, Beşiktaş"
            InputProps={{ sx: { bgcolor: '#fff' } }}
          />
          <Stack direction="row" spacing={1} alignItems="center" flexWrap="wrap">
            {statusLabels.map((s) => (
              <Chip
                key={s.key || 'all'}
                label={s.label}
                color={statusFilter === s.key ? 'primary' : 'default'}
                onClick={() => setStatusFilter(s.key)}
              />
            ))}
          </Stack>
        </Stack>
      </Stack>
    </Paper>
  );

  return (
    <Container maxWidth="lg">
      <PapperBlock
        title="Maçlar"
        icon="ios-football"
        desc="Güncel maçları takip et, tahmin yap, yorumları oku."
      >
        {hero}

        {loading && (
          <Typography color="text.secondary">Yükleniyor...</Typography>
        )}

        {error && (
          <Typography color="error" mb={2}>{error}</Typography>
        )}

        {!loading && !error && (
          <Grid container spacing={3}>
            {filteredMatches.map((match) => (
              <Grid item xs={12} md={6} key={match.id}>
                <Card elevation={3} sx={{ borderRadius: 2 }}>
                  <CardContent>
                    <Stack spacing={1.5}>
                      <Stack direction="row" spacing={1} alignItems="center" justifyContent="space-between">
                        <Stack direction="row" spacing={1} alignItems="center">
                          <SportsSoccerIcon fontSize="small" />
                          <Typography variant="h6" fontWeight="bold">
                            {match.homeTeam || 'Bilinmiyor'} vs {match.awayTeam || 'Bilinmiyor'}
                          </Typography>
                        </Stack>
                        <Chip
                          label={match.status || 'PLANLI'}
                          color={['FINISHED', 'BITTI'].includes((match.status || '').toUpperCase()) ? 'success' : 'default'}
                          size="small"
                        />
                      </Stack>

                      <Stack direction="row" spacing={2} alignItems="center" color="text.secondary">
                        {match.kickoffAt && (
                          <Stack direction="row" spacing={0.5} alignItems="center">
                            <CalendarMonthIcon fontSize="small" />
                            <Typography variant="body2">
                              {new Date(match.kickoffAt).toLocaleString('tr-TR')}
                            </Typography>
                          </Stack>
                        )}
                        {match.venue && (
                          <Stack direction="row" spacing={0.5} alignItems="center">
                            <PlaceIcon fontSize="small" />
                            <Typography variant="body2">{match.venue}</Typography>
                          </Stack>
                        )}
                      </Stack>

                      <Divider />
                      <Box display="flex" justifyContent="space-between" alignItems="center">
                        <Typography variant="body2" color="text.secondary">
                          Yorumlar ve detaylar için aç
                        </Typography>
                        <Button
                          variant="contained"
                          color="primary"
                          href={`/app/matches/${match.id}`}
                        >
                          Detaylara Git
                        </Button>
                      </Box>
                    </Stack>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}

        {!loading && !error && filteredMatches.length === 0 && (
          <Box textAlign="center" py={4}>
            <Typography variant="h6" color="text.secondary">
              Aramanızla eşleşen maç bulunamadı.
            </Typography>
          </Box>
        )}
      </PapperBlock>
    </Container>
  );
}

export default MatchesPage;
