import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  CircularProgress,
  Typography,
  Alert,
  Stack,
  Divider,
  TextField,
  Button,
  Rating,
  CardMedia,
  Chip,
  Grid,
  Avatar,
} from '@mui/material';
import { playersAPI } from 'utils/api';

function PlayerDetailPage() {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [score, setScore] = useState(7);
  const [comment, setComment] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    try {
      setLoading(true);
      const [playerRes, ratingRes] = await Promise.all([
        playersAPI.get(id),
        playersAPI.getRatings(id),
      ]);
      setData(playerRes);
      setRatings(ratingRes || []);
      setError(null);
    } catch (e) {
      setError('Veri yüklenemedi. Lütfen tekrar deneyin.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let mounted = true;
    (async () => {
      if (!mounted) return;
      await load();
    })();
    return () => {
      mounted = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleSubmit = async () => {
    try {
      setSubmitting(true);
      await playersAPI.rate(id, score, comment.trim());
      setComment('');
      await load();
    } catch (e) {
      setError('Değerlendirme eklenemedi. Lütfen tekrar deneyin.');
    } finally {
      setSubmitting(false);
    }
  };

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
        Oyuncu Detayı
      </Typography>

      <Card sx={{ mb: 2 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={4} md={3}>
            {data.imageUrl ? (
              <CardMedia
                component="img"
                image={data.imageUrl}
                alt={data.fullName}
                sx={{
                  height: { xs: 260, sm: 320 },
                  width: '100%',
                  objectFit: 'contain',
                  objectPosition: 'top center',
                  borderRadius: 1,
                }}
              />
            ) : (
              <Box py={3} display="flex" justifyContent="center">
                <Avatar sx={{ width: 120, height: 120, fontSize: 48 }}>
                  {data.fullName ? data.fullName[0] : '?'}
                </Avatar>
              </Box>
            )}
          </Grid>
          <Grid item xs={12} sm={8} md={9}>
            <CardContent>
              <Stack spacing={1.5}>
                <Stack direction="row" spacing={1} alignItems="center" flexWrap="wrap" useFlexGap>
                  <Typography variant="h5" fontWeight="bold">
                    {data.fullName}
                  </Typography>
                  {data.team && <Chip label={data.team} color="primary" size="small" />}
                  {data.position && <Chip label={data.position} variant="outlined" size="small" />}
                  {data.shirtNumber && <Chip label={`Forma ${data.shirtNumber}`} size="small" />}
                </Stack>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={3}>
                  <Stack spacing={0.5}>
                    <Typography color="text.secondary">Ortalama Puan</Typography>
                    <Stack direction="row" spacing={1} alignItems="center">
                      <Rating value={data.averageRating || 0} max={10} precision={0.1} readOnly />
                      <Typography variant="subtitle1" fontWeight="bold">
                        {(data.averageRating || 0).toFixed(1)}
                      </Typography>
                    </Stack>
                    <Typography variant="body2" color="text.secondary">
                      {data.ratingCount || 0} değerlendirme
                    </Typography>
                  </Stack>
                </Stack>
              </Stack>
            </CardContent>
          </Grid>
        </Grid>
      </Card>

      <Card variant="outlined" sx={{ mb: 2 }}>
        <CardContent>
          <Stack spacing={1.5}>
            <Typography variant="subtitle1">Değerlendirmeler</Typography>
            {(ratings || []).map((r) => (
              <Box key={r.id} pb={1}>
                <Stack direction="row" alignItems="center" spacing={1}>
                  <Rating size="small" value={Number(r.score) || 0} readOnly max={10} />
                  <Typography variant="body2" color="text.secondary">
                    {r.author || '-'}
                  </Typography>
                </Stack>
                {r.comment && (
                  <Typography variant="body2" color="text.primary">
                    {r.comment}
                  </Typography>
                )}
              </Box>
            ))}
            {(ratings || []).length === 0 && (
              <Typography color="text.secondary">Değerlendirme yok</Typography>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={1.5}>
            <Typography variant="subtitle1">Değerlendirme ekle</Typography>
            <Stack direction="row" alignItems="center" spacing={1}>
              <Rating
                value={score}
                max={10}
                onChange={(_, val) => setScore(val || 1)}
              />
              <Typography>{score}</Typography>
            </Stack>
            <TextField
              multiline
              minRows={2}
              fullWidth
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Yorum (opsiyonel)"
            />
            <Box display="flex" justifyContent="flex-end">
              <Button
                variant="contained"
                onClick={handleSubmit}
                disabled={submitting}
              >
                Gönder
              </Button>
            </Box>
          </Stack>
        </CardContent>
      </Card>

      <Box mt={2}>
        <Link to="/app/players">← Oyuncu listesine dön</Link>
      </Box>
    </Box>
  );
}

export default PlayerDetailPage;
