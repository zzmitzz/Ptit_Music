package com.example.musicapp.Function

import com.google.gson.annotations.SerializedName

data class MusicMetadata(
    @SerializedName("metadata") val metadata: Metadata,
    @SerializedName("status") val status: Status,
    @SerializedName("result_type") val resultType: Int
)

data class Metadata(
    @SerializedName("timestamp_utc") val timestampUtc: String,
    @SerializedName("music") val music: List<Music>
)

data class Music(
    @SerializedName("db_begin_time_offset_ms") val dbBeginTimeOffsetMs: Int,
    @SerializedName("db_end_time_offset_ms") val dbEndTimeOffsetMs: Int,
    @SerializedName("sample_begin_time_offset_ms") val sampleBeginTimeOffsetMs: Int,
    @SerializedName("sample_end_time_offset_ms") val sampleEndTimeOffsetMs: Int,
    @SerializedName("play_offset_ms") val playOffsetMs: Int,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("acrid") val acrid: String,
    @SerializedName("album") val album: Album,
    @SerializedName("rights_claim") val rightsClaim: List<RightsClaim>,
    @SerializedName("external_ids") val externalIds: ExternalIds,
    @SerializedName("result_from") val resultFrom: Int,
    @SerializedName("contributors") val contributors: Contributors,
    @SerializedName("title") val title: String,
    @SerializedName("langs") val langs: List<Lang>,
    @SerializedName("language") val language: String,
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("label") val label: String,
    @SerializedName("external_metadata") val externalMetadata: ExternalMetadata,
    @SerializedName("score") val score: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("works") val works: List<Work>
)

data class Artist(
    @SerializedName("name") val name: String,
    @SerializedName("langs") val langs: List<Lang>
)

data class Album(
    @SerializedName("name") val name: String,
    @SerializedName("langs") val langs: List<Lang>
)

data class RightsClaim(
    @SerializedName("distributor") val distributor: Distributor,
    @SerializedName("rights_owners") val rightsOwners: List<RightsOwner>,
    @SerializedName("rights_claim_policy") val rightsClaimPolicy: String,
    @SerializedName("territories") val territories: List<String>
)

data class Distributor(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class RightsOwner(
    @SerializedName("name") val name: String,
    @SerializedName("share_percentage") val sharePercentage: Int
)

data class ExternalIds(
    @SerializedName("iswc") val iswc: String,
    @SerializedName("isrc") val isrc: String,
    @SerializedName("upc") val upc: String
)

data class Contributors(
    @SerializedName("composers") val composers: List<String>,
    @SerializedName("lyricists") val lyricists: List<String>
)

data class Lang(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String
)

data class ExternalMetadata(
    @SerializedName("musicbrainz") val musicbrainz: Musicbrainz,
    @SerializedName("deezer") val deezer: Deezer,
    @SerializedName("spotify") val spotify: Spotify,
    @SerializedName("youtube") val youtube: Youtube
)

data class Musicbrainz(
    @SerializedName("track") val track: Track
)

data class Track(
    @SerializedName("id") val id: String
)

data class Deezer(
    @SerializedName("track") val track: Track,
    @SerializedName("artists") val artists: List<ArtistX>,
    @SerializedName("album") val album: AlbumX
)

data class ArtistX(
    @SerializedName("id") val id: String
)

data class AlbumX(
    @SerializedName("id") val id: String
)

data class Spotify(
    @SerializedName("track") val track: Track,
    @SerializedName("artists") val artists: List<ArtistX>,
    @SerializedName("album") val album: AlbumX
)

data class Youtube(
    @SerializedName("vid") val vid: String
)

data class Genre(
    @SerializedName("name") val name: String
)

data class Work(
    @SerializedName("iswc") val iswc: String,
    @SerializedName("name") val name: String,
    @SerializedName("creators") val creators: List<Creator>
)

data class Creator(
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("ipi") val ipi: Int,
    @SerializedName("roles") val roles: List<String>
)

data class Status(
    @SerializedName("msg") val msg: String,
    @SerializedName("version") val version: String,
    @SerializedName("code") val code: Int
)

