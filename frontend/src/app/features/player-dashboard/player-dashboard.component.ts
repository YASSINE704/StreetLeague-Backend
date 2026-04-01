import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';

export interface MatchInvitation {
  id: string;
  opponent: string;
  teamLogo: string;
  teamColor: string;
  venue: string;
  date: string;
  time: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  invitedBy: string;
}

export interface ActivityFeedItem {
  id: string;
  type: 'GOAL' | 'MATCH' | 'AWARD' | 'LEVEL';
  content: string;
  time: string;
  icon: string;
}

@Component({
  selector: 'app-player-dashboard',
  templateUrl: './player-dashboard.component.html',
  styleUrls: ['./player-dashboard.component.css']
})
export class PlayerDashboardComponent implements OnInit {
  profileForm!: FormGroup;
  activeTab: 'OVERVIEW' | 'PROFILE' | 'INVITATIONS' | 'STATS' = 'OVERVIEW';
  
  myTeam = {
    name: 'Urban Legends',
    logo: '🏙️',
    color: 'linear-gradient(135deg, #3b82f6, #1d4ed8)',
    rank: 4,
    members: [
      { name: 'Moi', pos: 'MID', avatar: 'M' },
      { name: 'Kévin', pos: 'FWD', avatar: 'K' },
      { name: 'Sarah', pos: 'DEF', avatar: 'S' },
      { name: 'Thomas', pos: 'GK', avatar: 'T' }
    ]
  };

  attributes = [
    { label: 'Vitesse', value: 85, color: '#3b82f6' },
    { label: 'Tir', value: 78, color: '#ef4444' },
    { label: 'Passe', value: 92, color: '#10b981' },
    { label: 'Dribble', value: 88, color: '#f59e0b' },
    { label: 'Défense', value: 65, color: '#6366f1' },
    { label: 'Physique', value: 74, color: '#8b5cf6' }
  ];

  activityFeed: ActivityFeedItem[] = [
    { id: '1', type: 'MATCH', content: 'Victoire 3-1 contre Warriors FC', time: 'Il y a 2h', icon: '🏆' },
    { id: '2', type: 'GOAL', content: 'Tu as marqué un triplé !', time: 'Hier', icon: '⚽' },
    { id: '3', type: 'LEVEL', content: 'Niveau 12 atteint : "Maître du Bitume"', time: '2 jours', icon: '📈' },
    { id: '4', type: 'AWARD', content: 'Badge "Fair Play" débloqué', time: '1 semaine', icon: '🤝' }
  ];

  invitations: MatchInvitation[] = [
    {
      id: 'inv1',
      opponent: 'Kings FC',
      teamLogo: '👑',
      teamColor: 'linear-gradient(135deg, #f59e0b, #d97706)',
      venue: 'Terrain Nord',
      date: 'Apr 12',
      time: '18:30',
      status: 'PENDING',
      invitedBy: 'Marcus Kane (Captain)'
    },
    {
      id: 'inv2',
      opponent: 'Swift Strikers',
      teamLogo: '⚡',
      teamColor: 'linear-gradient(135deg, #10b981, #059669)',
      venue: 'The Cage',
      date: 'Apr 15',
      time: '20:00',
      status: 'PENDING',
      invitedBy: 'Admin StreetLeague'
    }
  ];

  stats = {
    matchesPlayed: 42,
    goals: 18,
    assists: 12,
    rating: 7.8,
    cleanSheets: 0,
    winRate: 64
  };

  constructor(
    private fb: FormBuilder,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.user;
    this.profileForm = this.fb.group({
      name: [user?.username || '', Validators.required],
      username: [user?.username || '', Validators.required],
      email: [user?.email || '', [Validators.required, Validators.email]],
      position: ['MID', Validators.required],
      skillLevel: ['Intermédiaire', Validators.required],
      preferredFoot: ['Right', Validators.required],
      bio: ['Passionné par le street football. Toujours prêt pour un nouveau défi !'],
      availability: ['Soirs & Week-ends']
    });
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      console.log('Profile saved:', this.profileForm.value);
      alert('Profil mis à jour avec succès !');
    }
  }

  onAcceptInvitation(id: string): void {
    const inv = this.invitations.find(i => i.id === id);
    if (inv) inv.status = 'ACCEPTED';
  }

  onDeclineInvitation(id: string): void {
    const inv = this.invitations.find(i => i.id === id);
    if (inv) inv.status = 'DECLINED';
  }
}
