import {AfterViewInit, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import * as L from 'leaflet';
import {CoordinatesRanges, MapPoint} from '../../core/models/shared.model';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements AfterViewInit, OnChanges {

  @Input() points: MapPoint[] = [];
  @Output() pointClicked = new EventEmitter<{lat: number, lng: number}>();
  @Output() mapMoved = new EventEmitter<CoordinatesRanges>();
  private map!: L.Map;
  private markersLayer = L.layerGroup();
  startLat: number = 50.060671;
  startLng: number = 19.940817;

  ngAfterViewInit(): void {
    this.getUserLocation();
    this.initMap();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['points'] && this.map) {
      this.updateMarkers();
    }
  }

  private initMap(): void {
    this.map = L.map('map').setView([this.startLat, this.startLng], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.markersLayer.addTo(this.map);

    this.map.on('moveend', () => {
      this.mapMoved.emit(this.getCurrentCoords());
    });

    this.mapMoved.emit(this.getCurrentCoords());

    this.updateMarkers();

    setTimeout(() => {
      this.map.invalidateSize();
    }, 0);

  }

  private updateMarkers() {
    this.markersLayer.clearLayers();

    const pointIcon = L.icon({
      iconUrl: 'assets/icons8-map-point-48.png',
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32]
    });

    this.points.forEach(point => {
      const marker = L.marker([point.latitude, point.longitude], { icon: pointIcon })
        .bindPopup(point.name);

      marker.on('click', () => {
        this.pointClicked.emit({lat: point.latitude, lng: point.longitude});
      });

      marker.addTo(this.markersLayer);
    });
  }


  getCurrentCoords() {
    const bounds = this.map.getBounds();
    const sw = bounds.getSouthWest();
    const ne = bounds.getNorthEast();

    const coords: CoordinatesRanges = {
      minLatitude: sw.lat,
      maxLatitude: ne.lat,
      minLongitude: sw.lng,
      maxLongitude: ne.lng
    }
    return coords;
  }

  getUserLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.startLat = position.coords.latitude;
          this.startLng = position.coords.longitude;
        },
        error => {
          console.error('Błąd pobierania lokalizacji', error);
          this.startLat = 50.060671;
          this.startLng = 19.940817;
        }
      );
    } else {
      console.warn('Geolokalizacja nie jest wspierana');
      this.startLat = 50.060671;
      this.startLng = 19.940817;
    }
  }
}
