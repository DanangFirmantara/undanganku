import {
  Component, ChangeDetectionStrategy, OnInit, OnDestroy, AfterViewInit,
  signal, computed, HostListener
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterLink],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css'],
})
export class LandingComponent implements OnInit, AfterViewInit, OnDestroy {
  mobileMenuOpen  = signal(false);
  openingDone     = signal(false);
  scrollYRaw      = signal(0);
  scrollProgress  = signal(0);

  private countdownInterval: ReturnType<typeof setInterval> | null = null;
  private openingTimeout:    ReturnType<typeof setTimeout>  | null = null;
  private observer:          IntersectionObserver           | null = null;

  private targetDate = new Date('2025-12-15T08:45:00');

  days    = signal(0);
  hours   = signal(8);
  minutes = signal(45);
  seconds = signal(0);

  /* Subtle parallax — phone drifts opposite to scroll */
  phoneParallax = computed(() => `translateY(${this.scrollYRaw() * -0.06}px)`);

  /* Floating petal specs — individual properties to avoid style sanitization */
  readonly petals = Array.from({ length: 10 }, (_, i) => ({
    left:              `${6 + i * 9}%`,
    animationDelay:    `${(i * 2.3).toFixed(1)}s`,
    animationDuration: `${18 + (i % 4) * 5}s`,
    width:             `${5 + (i % 3) * 3}px`,
    height:            `${5 + (i % 3) * 3}px`,
    opacity:           `${(0.18 + (i % 3) * 0.07).toFixed(2)}`,
  }));

  features = [
    { icon: 'fa-bolt',               title: 'Selesai dalam 5 Menit',  description: 'Sistem instan kami memungkinkan Anda membuat undangan cantik hanya dengan mengisi formulir sederhana.' },
    { icon: 'fa-whatsapp',           title: 'Optimized WhatsApp',      description: 'Thumbnail undangan yang cantik saat link dibagikan di WhatsApp, meningkatkan antusiasme tamu Anda.' },
    { icon: 'fa-clock',              title: 'Countdown Otomatis',      description: 'Hitung mundur waktu acara secara real-time yang memicu kesan eksklusif dan mendebarkan bagi tamu.' },
    { icon: 'fa-envelope-open-text', title: 'Amplop Digital',          description: 'Terima kado digital langsung ke rekening Anda tanpa potongan biaya sedikitpun dari kami.' },
    { icon: 'fa-book-open',          title: 'Buku Ucapan',             description: 'Tamu dapat meninggalkan pesan hangat dan doa restu yang bisa Anda simpan selamanya secara digital.' },
    { icon: 'fa-music',              title: 'Musik Pengiring',         description: 'Pilihan instrumen premium yang akan otomatis terputar saat undangan dibuka untuk membangun suasana.' },
  ];

  steps = [
    { number: '1', title: 'Bayar Sekali',           description: 'Satu harga transparan tanpa biaya tersembunyi. Aktif selamanya.' },
    { number: '2', title: 'Isi Data Pernikahan',    description: 'Lengkapi detail acara, foto galeri, dan cerita cinta Anda melalui dashboard kami.' },
    { number: '3', title: 'Bagikan ke WhatsApp',    description: 'Undangan siap disebar ke seluruh keluarga dan kolega dengan satu klik.' },
  ];

  pricingFeatures = [
    'Akses Semua Tema Premium',
    'Fitur Amplop Digital & QR Code',
    'Pilihan Musik Latar & Galeri Foto',
    'Buku Ucapan & RSVP Real-time',
    'Thumbnail WhatsApp Custom',
    'Update Data Tanpa Batas',
  ];

  testimonials = [
    { quote: '"Tamu-tamu kami langsung terkesima dengan desainnya yang sangat elegan. Sangat membantu untuk mengelola kado digital dan RSVP."', name: 'Rizky & Nadia',  location: 'Padang, Sumatra Barat',  avatar: 'assets/landing/avatar-1.png' },
    { quote: '"Dashboardnya sangat user-friendly. Selesai buat dalam hitungan menit dan bisa langsung share. Sangat direkomendasikan!"',         name: 'Maya & Adit',   location: 'Jakarta Selatan',         avatar: 'assets/landing/avatar-2.png' },
    { quote: '"Pilihan musiknya sangat menenangkan dan estetik. Benar-benar memberikan sentuhan mewah pada undangan digital kami."',              name: 'Dimas & Putri', location: 'Surabaya, Jawa Timur',    avatar: 'assets/landing/avatar-3.png' },
  ];

  ngOnInit(): void {
    this.updateCountdown();
    this.countdownInterval = setInterval(() => this.updateCountdown(), 1000);
    /* Curtain stays 2.2 s then slides away */
    this.openingTimeout = setTimeout(() => this.openingDone.set(true), 2200);
  }

  ngAfterViewInit(): void {
    this.setupScrollReveal();
  }

  @HostListener('window:scroll')
  onScroll(): void {
    const y   = window.scrollY;
    const max = document.documentElement.scrollHeight - window.innerHeight;
    this.scrollYRaw.set(y);
    this.scrollProgress.set(max > 0 ? (y / max) * 100 : 0);
  }

  private setupScrollReveal(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        entries.forEach(e => {
          if (e.isIntersecting) {
            e.target.classList.add('is-visible');
            this.observer?.unobserve(e.target);
          }
        });
      },
      { threshold: 0.12, rootMargin: '0px 0px -48px 0px' }
    );
    document.querySelectorAll('[data-reveal]').forEach(el => this.observer!.observe(el));
  }

  ngOnDestroy(): void {
    if (this.countdownInterval) clearInterval(this.countdownInterval);
    if (this.openingTimeout)    clearTimeout(this.openingTimeout);
    this.observer?.disconnect();
  }

  private updateCountdown(): void {
    const diff = this.targetDate.getTime() - Date.now();
    if (diff <= 0) { this.days.set(0); this.hours.set(0); this.minutes.set(0); this.seconds.set(0); return; }
    this.days.set(Math.floor(diff / 86_400_000));
    this.hours.set(Math.floor((diff % 86_400_000) / 3_600_000));
    this.minutes.set(Math.floor((diff % 3_600_000) / 60_000));
    this.seconds.set(Math.floor((diff % 60_000) / 1_000));
  }

  toggleMobileMenu(): void  { this.mobileMenuOpen.update(v => !v); }
  closeMobileMenu(): void   { this.mobileMenuOpen.set(false); }

  scrollToSection(id: string): void {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    this.closeMobileMenu();
  }
}
